package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.entity.Student;
import uz.pdp.appjparelationships.entity.Subject;
import uz.pdp.appjparelationships.payload.StudentDto;
import uz.pdp.appjparelationships.repository.AddressRepository;
import uz.pdp.appjparelationships.repository.GroupRepository;
import uz.pdp.appjparelationships.repository.StudentRepository;
import uz.pdp.appjparelationships.repository.SubjectRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    SubjectRepository subjectRepository;

    //1. VAZIRLIK
    @GetMapping("/forMinistry")
    public Page<Student> getStudentListForMinistry(@RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAll(pageable);
        return studentPage;
    }

    //2. UNIVERSITY
    @GetMapping("/forUniversity/{universityId}")
    public Page<Student> getStudentListForUniversity(@PathVariable Integer universityId,
                                                     @RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAllByGroup_Faculty_UniversityId(universityId, pageable);
        return studentPage;
    }

    //3. FACULTY DEKANAT
    @GetMapping("/forFaculty/{faculdyId}")
    public Page<Student> getStudentByFacultyId(@PathVariable Integer facultyId,@RequestParam int page){
        Pageable pageable = PageRequest.of(page,10);
        Page<Student> students = studentRepository.findAllByGroup_FacultyId(facultyId, pageable);
        return students;
    }

    //4. GROUP OWNER
    @GetMapping("/forGroup/{groupId}")
    public Page<Student> getStudentByGroupId(@PathVariable Integer groupId,@RequestParam int page){
        Pageable pageable = PageRequest.of(page,10);
        Page<Student> students = studentRepository.findAllByGroupId(groupId,pageable);
        return students;
    }


    @PostMapping("/student")
    public String addStudent(@RequestBody StudentDto studentDto){
        Student student = new Student();
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        Optional<Address> optionalAddress = addressRepository.findById(studentDto.getAddressId());
        if (!optionalAddress.isPresent()){
            return "address not found";
        }
        student.setAddress(optionalAddress.get());
        Optional<Group> optionalGroup = groupRepository.findById(studentDto.getGroupId());
        if (!optionalGroup.isPresent()){
            return "group not found";
        }
        student.setGroup(optionalGroup.get());
        List<Subject> subjectList = subjectRepository.findAllById(studentDto.getSubjectsId());
        if (subjectList.isEmpty()){
            return "subject list is not found";
        }
        student.setSubjects(subjectList);
        studentRepository.save(student);
        return "student added";
    }

    @DeleteMapping("/deleteStudent/{id}")
    public String deleteStudent(@PathVariable Integer id){
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()){
            studentRepository.deleteById(id);
            return "Student deleted";
        }
        return "student not found";
    }

    @PutMapping("/editStudent/{id}")
    public String editStudent(@PathVariable Integer id,@RequestBody StudentDto studentDto){
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (!optionalStudent.isPresent()){
            return "student not found";
        }
        Student student = optionalStudent.get();
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        Optional<Address> optionalAddress = addressRepository.findById(studentDto.getAddressId());
        if (!optionalAddress.isPresent()){
            return "Address not found";
        }
        student.setAddress(optionalAddress.get());
        Optional<Group> optionalGroup = groupRepository.findById(studentDto.getGroupId());
        if (!optionalGroup.isPresent()){
            return "group not found";
        }
        student.setGroup(optionalGroup.get());
        List<Subject> subjectList = subjectRepository.findAllById(studentDto.getSubjectsId());
        if (subjectList.isEmpty()){
            return "subjects not found or error";
        }
        student.setSubjects(subjectList);
        studentRepository.save(student);
        return "student edited";
    }
}
