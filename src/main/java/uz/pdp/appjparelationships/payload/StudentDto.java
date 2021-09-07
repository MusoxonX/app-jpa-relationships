package uz.pdp.appjparelationships.payload;

import lombok.Data;

import javax.persistence.Column;
import java.util.List;

@Data
public class StudentDto {
    private String firstName;
    private String lastName;
    private Integer addressId;
    private Integer groupId;
    private List<Integer> subjectsId;

}
