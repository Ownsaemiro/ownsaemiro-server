package org.dongguk.ownsaemiro.ownsaemiroserver.repository;

import org.dongguk.ownsaemiro.ownsaemiroserver.domain.UserWalletHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserWalletHistoryRepository extends JpaRepository<UserWalletHistory, Long> {
}
