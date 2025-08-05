package system.jobs;

import dao.UserTokenDAO;
import dto.CleanerRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Setter
public class CleanerJob implements Job{

    private volatile boolean enabled = false;
    private final UserTokenDAO userTokenDAO;

    @Override
    public void execute() {
        userTokenDAO.cleanExpiredTokens();
    }

    @Scheduled(fixedRate = 1 * 60 * 1000) // 1 min
    public void scheduledExecute() {
        if (!enabled) {
            return;
        }
        System.out.println("Running token cleaner task...");
        execute();
    }

}
