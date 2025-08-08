package MaslyakBank_Core.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maslyakbank/super-admin")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class SuperAdminController {
}
