package com.example.lab7_recyclerview;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText edtName, edtEmail;
    private Button btnAdd, btnUpdate, btnDelete;
    private RecyclerView recyclerView;

    private DatabaseHelper db;
    private StudentAdapter adapter;
    private List<Student> studentList;

    private int selectedStudentId = -1; // Biến lưu ID khi người dùng click vào dòng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        btnAdd = findViewById(R.id.btnAdd);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        recyclerView = findViewById(R.id.recyclerView);

        db = new DatabaseHelper(this);

        // YÊU CẦU: Hiển thị danh sách 10 sinh viên từ SQLite
        // Nếu database trống, tự động thêm 10 sinh viên mặc định
        if (db.getStudentsCount() == 0) {
            for (int i = 1; i <= 10; i++) {
                db.addStudent(new Student("Sinh Viên " + i, "sinhvien" + i + "@gmail.com"));
            }
        }

        // Cấu hình RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadData();

        // 1. Chức năng THÊM
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                String email = edtEmail.getText().toString();
                if (!name.isEmpty() && !email.isEmpty()) {
                    db.addStudent(new Student(name, email));
                    clearFields();
                    loadData();
                    Toast.makeText(MainActivity.this, "Đã thêm thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 2. Chức năng SỬA
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedStudentId != -1) {
                    String name = edtName.getText().toString();
                    String email = edtEmail.getText().toString();
                    db.updateStudent(new Student(selectedStudentId, name, email));
                    clearFields();
                    loadData();
                    Toast.makeText(MainActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Vui lòng chọn 1 sinh viên bên dưới để sửa", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 3. Chức năng XÓA
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedStudentId != -1) {
                    db.deleteStudent(selectedStudentId);
                    clearFields();
                    loadData();
                    Toast.makeText(MainActivity.this, "Đã xóa sinh viên!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Vui lòng chọn 1 sinh viên bên dưới để xóa", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Hàm load dữ liệu lên RecyclerView
    private void loadData() {
        studentList = db.getAllStudents();
        adapter = new StudentAdapter(studentList, new StudentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Student student) {
                // Khi click vào 1 dòng, đưa dữ liệu lên EditText và lưu lại ID
                edtName.setText(student.getName());
                edtEmail.setText(student.getEmail());
                selectedStudentId = student.getId();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    // Hàm reset form
    private void clearFields() {
        edtName.setText("");
        edtEmail.setText("");
        selectedStudentId = -1;
    }
}