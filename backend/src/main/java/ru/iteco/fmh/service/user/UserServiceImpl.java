package ru.iteco.fmh.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.iteco.fmh.dao.repository.UserRepository;
import ru.iteco.fmh.dao.repository.UserRoleClaimRepository;
import ru.iteco.fmh.dto.user.UserShortInfoDto;
import ru.iteco.fmh.exceptions.IncorrectDataException;
import ru.iteco.fmh.exceptions.InvalidLoginException;
import ru.iteco.fmh.exceptions.NotFoundException;
import ru.iteco.fmh.model.user.User;

import java.util.List;
import java.util.stream.Collectors;

import static ru.iteco.fmh.model.user.RoleClaimStatus.CONFIRMED;
import static ru.iteco.fmh.model.user.RoleClaimStatus.NEW;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ConversionService conversionService;
    private final UserRoleClaimRepository userRoleClaimRepository;

    @Override
    public List<UserShortInfoDto> getAllUsers(PageRequest pageRequest, Boolean showConfirmed) {
        if (showConfirmed == null) {
            return userRepository.findAll(pageRequest).getContent().stream()
                    .map(i -> conversionService.convert(i, UserShortInfoDto.class)).collect(Collectors.toList());
        } else if (!showConfirmed) {
            return userRepository.findAllByRoleClaimIsNewOrRejected(pageRequest).stream()
                    .map(i -> conversionService.convert(i, UserShortInfoDto.class)).collect(Collectors.toList());
        } else {
            return userRepository.findAllByRoleClaimIsConfirmedOrNull(pageRequest).stream()
                    .map(i -> conversionService.convert(i, UserShortInfoDto.class)).collect(Collectors.toList());
        }
    }

    @Override
    public User getActiveUserByLogin(String login) {
        User user = userRepository.findUserByLogin(login);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (user.isDeleted()) {
            throw new InvalidLoginException("Пользователь удален, обратитесь к администратору!");
        }
        return user;
    }

    @Override
    public UserShortInfoDto confirmUserRole(int userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден"));
        var userRoleClaim = userRoleClaimRepository.findByUserId(userId)
                .stream().filter(el -> el.getStatus() == NEW).findFirst()
                .orElseThrow(() -> new IncorrectDataException("У данного пользователя нет заявок на подтверждение роли"));
        user.getUserRoles().add(userRoleClaim.getRole());
        userRoleClaim.setStatus(CONFIRMED);
        userRepository.save(user);
        userRoleClaimRepository.save(userRoleClaim);
        return conversionService.convert(user, UserShortInfoDto.class);
    }
}
