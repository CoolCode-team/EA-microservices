package user.user.domain.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import user.user.domain.repository.UserRepository;

@Service
public class MetricService {

    private final UserRepository userRepository;

    @Autowired
    public MetricService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Map<String, Long> getUserCount() {
        return Map.of("users", userRepository.count());
    }
}