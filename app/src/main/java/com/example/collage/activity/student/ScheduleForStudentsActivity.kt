package com.example.collage.activity.student

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
import com.example.collage.activity.student.SubjectAboutActivity
import com.example.collage.database.AppDatabase
import com.example.collage.models.SubjectTime
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

class ScheduleForStudentsActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var adapter: SchedulesAdapter
    private var studentId: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_schedule_for_students)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        db = AppDatabase.getDatabase(applicationContext)

        val lv = findViewById<ListView>(R.id.lSubjectsForStudents)
        adapter = SchedulesAdapter(this)

        lv.adapter = adapter
        loadSchedules()
        loadStudentGroup()
        findViewById<Button>(R.id.bBackStudentCard).setOnClickListener {
            finish()
        }
    }

    private fun loadSchedules(){
        lifecycleScope.launch {
            val allSchedulesService = db.subjectsTimeDao().getAllSubjectsTime()
            adapter.submitList(allSchedulesService)
        }
    }
    private fun loadStudentGroup(){
        studentId = SessionManager.getStudentId(this)
        lifecycleScope.launch {
            val groupId = db.userDao().getUserFromId(studentId).groupId
            findViewById<TextView>(R.id.tStudentGroup).text =
                db.groupDao().getGroupFromId(groupId).name
        }
    }

    inner class SchedulesAdapter(context: Context): BaseAdapter(){
        private val inflater = LayoutInflater.from(context)
        private var scheduleService = emptyList<SubjectTime>()
        private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        fun submitList(newList: List<SubjectTime>){
            scheduleService = newList
            notifyDataSetChanged()
        }

        override fun getCount(): Int = scheduleService.size

        override fun getItem(p0: Int): SubjectTime = scheduleService[p0]

        override fun getItemId(p0: Int): Long = scheduleService[p0].subjectId.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: inflater.inflate(R.layout.subject_card_for_students, parent, false)
            val item = getItem(position)

            val intent = Intent(applicationContext, SubjectAboutActivity::class.java).apply {
                putExtra("SUBJECT_ID", item.subjectId)
            }

            lifecycleScope.launch {
                view.findViewById<TextView>(R.id.tSubjectStudent).text =
                    db.subjectDao().getSubjectById(item.subjectId).name

                view.findViewById<TextView>(R.id.tLessonStart).text =
                    "Начало: ${item.lessonStart.format(timeFormatter)}"
                view.findViewById<TextView>(R.id.tLessonEnd).text =
                    "Конец: ${item.lessonEnd.format(timeFormatter)}"

                view.findViewById<Button>(R.id.bAboutSubject).setOnClickListener {
                    startActivity(intent)
                }
            }

            return view
        }
    }
}