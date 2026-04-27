package iuh.fit.se.group1.dto;

import lombok.*;

import java.io.File;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ImportRequest implements Serializable {
    protected File file;
}
