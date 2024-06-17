package com.beneboba.package_tracking.repository;

import com.beneboba.package_tracking.entity.Sender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SenderRepository extends JpaRepository<Sender, Long> {
}
