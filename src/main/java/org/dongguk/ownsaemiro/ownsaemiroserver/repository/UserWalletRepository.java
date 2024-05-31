package org.dongguk.ownsaemiro.ownsaemiroserver.repository;

import org.dongguk.ownsaemiro.ownsaemiroserver.domain.UserWallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserWalletRepository extends JpaRepository<UserWallet, Long> {
}
