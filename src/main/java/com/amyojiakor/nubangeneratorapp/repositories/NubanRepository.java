package com.amyojiakor.nubangeneratorapp.repositories;

import com.amyojiakor.nubangeneratorapp.models.entities.NubanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NubanRepository extends JpaRepository<NubanEntity, Long> {
}
