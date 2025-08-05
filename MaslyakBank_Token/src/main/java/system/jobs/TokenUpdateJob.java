package system.jobs;

import dao.UserTokenDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenUpdateJob implements Job{

    private final UserTokenDAO userTokenDAO;

    @Override
    public void execute() {
        userTokenDAO.updateExpiredTokens();
    }

    @Scheduled(fixedRate = 1 * 60 * 1000) // 1 min
    public void scheduledExecute() {
        System.out.println("Running token updater task...");
        execute();
    }
}
