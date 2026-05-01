package iuh.fit.se.group1.dto;

import lombok.*;

import javax.swing.*;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ExportRequest implements Serializable {
    private List<List<Object>> data;
    private String title;
    private boolean excludeLastColumn;
    private List<String> columnHeaders;
}
