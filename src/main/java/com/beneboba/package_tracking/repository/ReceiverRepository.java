package com.beneboba.package_tracking.repository;

import com.beneboba.package_tracking.entity.Receiver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiverRepository extends JpaRepository<Receiver, Long> {
}
