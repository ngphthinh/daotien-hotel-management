package iuh.fit.se.group1.dto;

import lombok.*;

import javax.swing.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ExportRequest implements Serializable {
    private JTable table;
    private String title;
    private boolean excludeLastColumn;
}
