package com.panchmukhi.trading.repository;

import com.panchmukhi.trading.model.SectorIntelligence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SectorIntelligenceRepository extends JpaRepository<SectorIntelligence, String> {

    Optional<SectorIntelligence> findBySectorName(String sectorName);
}
