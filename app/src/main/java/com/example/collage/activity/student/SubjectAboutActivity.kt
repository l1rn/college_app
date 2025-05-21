package com.example.collage.activity.student

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.collage.R
import com.example.collage.SessionManager
import com.example.collage.activity.teacher.TeachersScheduleForStudentsActivity
import com.example.collage.database.AppDatabase
import com.example.collage.models.Subject
import com.example.collage.models.SubjectTime
import com.example.collage.models.User
import kotlinx.coroutines.launch

class SubjectAboutActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private var subjectId: Int = -1
    private var teacherId: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_subject_about)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = AppDatabase.getDatabase(this)

        subjectId = intent.getIntExtra("SUBJECT_ID", -1)


        if(subjectId == -1){
            finish()
            return
        }

        finishSubjectCard()
        loadActivity()
    }

    private fun finishSubjectCard(){
        findViewById<Button>(R.id.bBackFromSubject).setOnClickListener{
            finish()
        }
    }

    private fun loadActivity() {
        lifecycleScope.launch {
            val studentId = getCurrentStudentId()
            val grades = db.gradeDao().getGrades(studentId, subjectId)
            val marksList = grades.joinToString(", ") { it.value.toString() }
            val subjectTime: SubjectTime = db.subjectsTimeDao().getSubjectById(subjectId)

            teacherId = subjectTime.teacherId!!

            if(teacherId == 0){
                finish()
            }

            findViewById<TextView>(R.id.tSubjectTitle).text = db.subjectDao().getSubjectById(subjectId).name
            if(marksList.isEmpty()){
                findViewById<TextView>(R.id.tAllMarks).text = "Нету"
            }
            else{
                findViewById<TextView>(R.id.tAllMarks).text = marksList
            }
            findViewById<TextView>(R.id.tTeacherName).text = db.userDao().getUserFromId(teacherId).name

            val intent = Intent(applicationContext, TeachersScheduleForStudentsActivity::class.java).apply {
                putExtra("TEACHER_ID", teacherId)
            }

            findViewById<Button>(R.id.bToTeacherCard).setOnClickListener{
                startActivity(intent)
            }
        }
    }

    private fun getCurrentStudentId(): Int{
        var studentId: Int = -1

        studentId = SessionManager.getStudentId(applicationContext)
        if(studentId != -1){
            return studentId
        }

        return 1
    }
}