package iuh.fit.se.group1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RoomSelection implements Serializable {
    private Long id;
    private String number;
    private String roomType;


    @Override
    public String toString() {
        return number;
    }
}