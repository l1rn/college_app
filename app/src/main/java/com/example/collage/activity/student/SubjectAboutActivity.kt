package com.example.collage.activity.student

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.collage.R
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
        loadActivity(applicationContext)
    }

    private fun finishSubjectCard(){
        findViewById<Button>(R.id.bBackFromSubject).setOnClickListener{
            finish()
        }
    }

    private fun loadActivity(context: Context) {
        lifecycleScope.launch {
////            teacherId = db.subjectsTimeDao().getTeacherIdById(subjectId)
//            val teacher: User = db.userDao().getUserFromId(teacherId)
//
//            val subjectTime: SubjectTime = db.subjectsTimeDao().getSubjectById(subjectId)
//            val subject: Subject = db.subjectDao().getSubjectById(subjectTime.subjectId)
//
////            val marks: List<Int> = db.subjectDao().getMarksById(subjectId)
//            findViewById<TextView>(R.id.tSubjectTitle).text = subject.name
//
////            findViewById<TextView>(R.id.tAllMarks).text = marks.joinToString(", ")
//            findViewById<TextView>(R.id.tTeacherName).text = teacher.name
        }
    }
}