package org.dongguk.ownsaemiro.ownsaemiroserver.repository;

import org.dongguk.ownsaemiro.ownsaemiroserver.domain.User;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.ERole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.isBanned = true")
    Page<User> findAllByIsBanned(Pageable pageable);

    Optional<User> findById(Long id);

    Optional<User> findBySerialId(String serialId);

    Boolean existsBySerialId(String serialId);

    @Query("select u.id as id, u.role as role, u.password as password from User u where u.serialId = :serialId")
    Optional<UserSecurityForm> findUserSecurityFromBySerialId(String serialId);

    @Query("select u.id as id, u.role as role, u.password as password from User u where u.id = :id")
    Optional<UserSecurityForm> findUserSecurityFromById(Long id);

    interface UserSecurityForm {
        Long getId();
        ERole getRole();
        String getPassword();
        static UserSecurityForm invoke(User user){
            return new UserSecurityForm() {
                @Override
                public Long getId() {
                    return user.getId();
                }

                @Override
                public ERole getRole() {
                    return user.getRole();
                }

                @Override
                public String getPassword() {
                    return user.getPassword();
                }
            };
        }
    }
}
