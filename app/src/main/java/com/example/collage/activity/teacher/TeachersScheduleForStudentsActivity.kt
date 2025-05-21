package com.example.collage.activity.teacher

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.collage.R
import com.example.collage.SessionManager
import com.example.collage.database.AppDatabase
import com.example.collage.models.SubjectTime
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

class TeachersScheduleForStudentsActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var adapter: TeacherScheduleAdapter
    private var studentId: Int = -1
    private var teacherId: Int = -1
    private var teacherLogin: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_teachers_schedule_for_students)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        db = AppDatabase.getDatabase(applicationContext)
        if (SessionManager.getTeacherId(applicationContext) != -1) {
            teacherLogin = true
        }else{
            teacherLogin = false
        }

        if (teacherLogin) {
            teacherId = SessionManager.getTeacherId(applicationContext)
        }
        else {
            teacherId = intent.getIntExtra("TEACHER_ID", -1)
        }

        println("Teacher id: $teacherId, teacherLogin: $teacherLogin")

        studentId = SessionManager.getStudentId(applicationContext)

        if(teacherId == -1){
            finish()
            return
        }

        adapter = TeacherScheduleAdapter(this)

        val listView: ListView = findViewById<ListView>(R.id.lTeacherSchedule)
        loadTeacherSchedule()
        listView.adapter = adapter
        back()
    }

    private fun loadTeacherSchedule(){
        lifecycleScope.launch {
            val teacherSchedule: List<SubjectTime> = db.subjectsTimeDao().getAllSubjectsTimeByTeacherId(teacherId)
            adapter.submitList(teacherSchedule)
        }
    }

    private fun back(){
        findViewById<Button>(R.id.bBackFromTeacherSchedule).setOnClickListener {
            finish()
        }
    }


    inner class TeacherScheduleAdapter(context: Context): BaseAdapter(){
        private val inflater = LayoutInflater.from(context)
        private var scheduleTeacher = emptyList<SubjectTime>()
        private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        fun submitList(newList: List<SubjectTime>){
            scheduleTeacher = newList
            notifyDataSetChanged()
        }

        override fun getCount(): Int = scheduleTeacher.size

        override fun getItem(p0: Int): SubjectTime = scheduleTeacher[p0]

        override fun getItemId(p0: Int): Long = scheduleTeacher[p0].subjectId.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: inflater.inflate(R.layout.subject_card_schedule_for_teachers, parent, false)
            val item = getItem(position)

            lifecycleScope.launch {
                try {
                    view.findViewById<TextView>(R.id.tGroup).text =
                        db.groupDao().getGroupFromId(item.groupId).name

                    view.findViewById<TextView>(R.id.tSubjectTeacher).text =
                        db.subjectDao().getSubjectById(item.subjectId).name

                    val button = findViewById<Button>(R.id.bAboutSubjectTeacher)

                    button.visibility = if (teacherLogin) View.VISIBLE else View.INVISIBLE
                    button.setOnClickListener {
                        SessionManager.saveSubjectId(applicationContext, item.subjectId)
                        startActivity(Intent(applicationContext, SubjectAboutForTeacherActivity::class.java))
                    }
                }
                catch (e: Exception){
                    e.printStackTrace()
                }

            }
            view.findViewById<TextView>(R.id.tLessonStartTeacher).text =
                "Начало: ${item.lessonStart.format(timeFormatter)}"
            view.findViewById<TextView>(R.id.tLessonEndTeacher).text =
                "Конец: ${item.lessonEnd.format(timeFormatter)}"
            return view
        }

    }
}