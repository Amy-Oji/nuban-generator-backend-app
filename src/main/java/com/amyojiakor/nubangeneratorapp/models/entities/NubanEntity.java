package com.amyojiakor.nubangeneratorapp.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@Entity
public class NubanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bankCode;
    private String serialNum;
    private String generatedNuban;
    private LocalDateTime dateTime;
}
