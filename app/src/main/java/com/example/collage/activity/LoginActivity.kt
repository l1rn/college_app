package com.example.collage.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.collage.R
import com.example.collage.SessionManager
import com.example.collage.activity.student.ScheduleForStudentsActivity
import com.example.collage.activity.student.StudentActivity
import com.example.collage.activity.teacher.TeacherActivity
import com.example.collage.activity.teacher.TeachersScheduleForStudentsActivity
import com.example.collage.database.AppDatabase
import com.example.collage.models.Role
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.bBackLogin).setOnClickListener {
            finish()
        }
        db = AppDatabase.getDatabase(this)

        findViewById<Button>(R.id.bLogin).setOnClickListener {
            checkRole(findViewById<EditText>(R.id.ePhoneLogin).text.toString(), applicationContext)
        }
    }

    private fun checkRole(phone: String, context: Context){
        lifecycleScope.launch {
            val phoneExists = db.userDao().isUserExists(phone)
            if(phoneExists){
                val role = db.userDao().getRoleFromPhone(phone)
                println(role)

                SessionManager.clearSession(context)

                if(role == Role.STUDENT){
                    SessionManager.saveStudentId(context, db.userDao().getUserIdFromPhone(phone))
                }
                else if(role == Role.TEACHER){
                    SessionManager.saveTeacherId(context, db.userDao().getUserIdFromPhone(phone))
                }

                when (role) {
                    Role.STUDENT -> startActivity(Intent(context, ScheduleForStudentsActivity::class.java))
                    Role.TEACHER -> startActivity(Intent(context, TeachersScheduleForStudentsActivity::class.java))
                }
            }
            else{
                Toast.makeText(context, "Неправильный логин или пароль", Toast.LENGTH_SHORT).show()

            }
        }
    }
}