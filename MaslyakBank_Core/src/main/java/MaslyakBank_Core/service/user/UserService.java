package MaslyakBank_Core.service.user;


import MaslyakBank_Core.dao.UserFilterRepository;
import MaslyakBank_Core.dao.UserPageDAO;
import MaslyakBank_Core.dao.UserSecurityDAO;
import MaslyakBank_Core.dto.requests.JwtTokenRequestDTO;
import MaslyakBank_Core.dto.requests.RegistrationRequestDTO;
import MaslyakBank_Core.dto.requests.UserFilterDTO;
import MaslyakBank_Core.dto.response.ResponsePaginationOffsetDTO;
import MaslyakBank_Core.mappers.UserMapper;
import MaslyakBank_Core.system.UserSpecifications;
import dto.TokenPair;
import entity.UsersTable;
import enums.UserRole;
import enums.UserStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClient;
import util.SecurityUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;



@Service
public class UserService {

    private final RestClient tokenRestClient;
    private final UserSecurityDAO userDAO;
    private final UserPageDAO userPageDAO;
    private final UserFilterRepository userFilterRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;


    public UserService(@Qualifier("tokenRestClient") RestClient tokenRestClient, UserSecurityDAO userDAO, UserPageDAO userPageDAO, UserFilterRepository userFilterRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, RedisTemplate<String, Object> redisTemplate) {
        this.tokenRestClient = tokenRestClient;
        this.userDAO = userDAO;
        this.userPageDAO = userPageDAO;
        this.userFilterRepository = userFilterRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.redisTemplate = redisTemplate;
    }

    public TokenPair requestRegistrationToken(RegistrationRequestDTO dto) {
        registerUser(dto);
        return sendTokenRequest(dto.getLogin(), "/create");
    }

    public void registerUser(RegistrationRequestDTO dto) {
        UsersTable user = userMapper.toEntity(dto);
        user.setPasswordSalt(passwordEncoder.encode(dto.getPassword()));
        user.setStatus(UserStatus.REGISTERED);
        user.setRole(UserRole.USER);
        userDAO.registrationUser(user);
    }


    public TokenPair sendAuthRequest(JwtTokenRequestDTO dto) {
        return tokenRestClient.post()
                .uri("/auth/create")
                .body(dto)
                .retrieve()
                .body(TokenPair.class);
    }

    public void sendLogoutRequest() {//todo refactor method (not SRP)
        String token = SecurityUtil.getCurrentToken();
        String login = SecurityUtil.getCurrentUser().getLogin();
        redisTemplate.opsForValue().set(
                    "token: " + token,
                    login,
                    5 * 60 * 1000, //5 минут храниться токен в блек листе
                    TimeUnit.MILLISECONDS
            );
        tokenRestClient.post()
                .uri("/logout")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .toBodilessEntity();
    }

    private TokenPair sendTokenRequest(String login, String uri) {
        return tokenRestClient.post()
                .uri(uri)
                .body(login)
                .retrieve()
                .body(TokenPair.class);
    }

    public ResponsePaginationOffsetDTO getUsersOffset(UserFilterDTO filter, int page) {
        int size = 10;
        Pageable pageable = PageRequest.of(page - 1, size);

        Specification<UsersTable> spec = UserSpecifications.buildFromFilter(filter);

        int totalElements = (int) userFilterRepository.count(spec);
        int totalPages = (int) Math.ceil((double) totalElements / size);

        List<UsersTable> users = userFilterRepository.findAll(spec, pageable).getContent();

        return new ResponsePaginationOffsetDTO(page, size, totalPages, totalElements, users);
    }

    public List<UsersTable> getFirstPage(int limit) {
        return userPageDAO.getFirstPage(limit);
    }
    public List<UsersTable> getNextPage(int limit, String cursor) {
        UsersTable lastUser = userDAO.findByLogin(cursor);
        return userPageDAO.getNextPage(limit, lastUser);
    }


}