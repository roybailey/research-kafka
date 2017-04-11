package me.roybailey.springboot.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Todo {

    String key;
    @JsonDeserialize
    LocalDate dueDate;
    String title;

}
