package com.example.collage.activity.teacher

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.collage.R
import com.example.collage.SessionManager
import com.example.collage.dao.GroupDao
import com.example.collage.database.AppDatabase
import com.example.collage.models.Grade
import com.example.collage.models.User
import kotlinx.coroutines.launch
import java.util.Calendar

class SubjectAboutForTeacherActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var groupDao: GroupDao
    private lateinit var adapter: StudentsAdapter

    private var subjectId: Int = -1
    private var teacherId: Int = -1
    private var groupId: Int = -1

    private var users: List<User> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_subject_about_for_teacher)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        db = AppDatabase.getDatabase(this)
        groupDao = db.groupDao()

        subjectId = SessionManager.getSubjectId(applicationContext)
        teacherId = SessionManager.getTeacherId(applicationContext)

        if(subjectId == -1){
            finish()
            return
        }

        val listView = findViewById<ListView>(R.id.allStudents)
        adapter = StudentsAdapter(this)

        loadStudents()
        listView.adapter = adapter
        back()
        confirmButton()
    }

    private fun back(){
        findViewById<Button>(R.id.bBackFromSubjectCard).setOnClickListener {
            SessionManager.removeSubjectId(applicationContext)
            finish()
        }
    }

    private fun loadStudents(){
        lifecycleScope.launch {
            groupId = db.subjectsTimeDao().getSubjectById(subjectId).groupId
            users = db.userDao().getAllUsersByGroupId(groupId)

            findViewById<TextView>(R.id.tSubjectLabel).text =
                db.subjectDao().getSubjectById(subjectId).name

            adapter.submitList(users)
        }
    }

    private fun confirmButton(){
        findViewById<Button>(R.id.addStudentMark).setOnClickListener {
            val usersName = users.map { it.name }.toTypedArray()
            val selectedNames = BooleanArray(usersName.size)

            val container = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(32, 16, 32, 16)
            }
            val dateEditText = EditText(this).apply {
                hint = "Введите дату"
                isFocusable = false
                setOnClickListener {
                    showDatePickerDialog(this)
                }
            }

            val setMark = EditText(this).apply {
                hint = "Введите оценку"
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }

            container.addView(dateEditText)
            container.addView(setMark)

            val alertDialog = AlertDialog.Builder(this)
                .setTitle("Ставим / Оценку")
                .setView(container)
                .setMultiChoiceItems(usersName, selectedNames) { _, which, isChecked ->
                    selectedNames[which] = isChecked
                }
                .setPositiveButton("Подтвердить"){ dialog, _ ->
                    val selectedUserIds = selectedNames
                        .mapIndexed { index, selected -> if (selected) index else -1 }
                        .filter { it != -1 }
                        .map { users[it].userId }
                    if(selectedUserIds.isEmpty()){
                        Toast.makeText(this, "Выберите хотя бы одного пользователя", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }

                    val date = dateEditText.text.toString()
                    val mark = setMark.text.toString()
                    if(mark.isBlank() || !mark.matches(Regex("\\d+"))){
                        Toast.makeText(this, "Введите корректную оценку", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }
                    val markValue = mark.toInt()

                    selectedUserIds.forEach { selectedUserId ->
                        val grade = Grade(
                            studentId = selectedUserId,
                            subjectId = subjectId,
                            value = markValue,
                            date = date
                        )
                        lifecycleScope.launch {
                            db.gradeDao().insert(grade)
                        }
                    }
                    Toast.makeText(this, "Оценки сохранены", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Отмена"){ dialog, _ ->
                    dialog.dismiss()
                }
                .create()
            alertDialog.show()
        }
    }

    inner class StudentsAdapter(context: Context): BaseAdapter(){
        private val inflater = LayoutInflater.from(context)
        private var studentsService = emptyList<User>()

        fun submitList(newList: List<User>){
            studentsService = newList
            notifyDataSetChanged()
        }

        override fun getCount(): Int = studentsService.size

        override fun getItem(p0: Int): User = studentsService[p0]

        override fun getItemId(p0: Int): Long = studentsService[p0].userId.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: inflater.inflate(R.layout.student_card_for_teacher, parent, false)
            val item = getItem(position)

            view.findViewById<TextView>(R.id.tStudentId).text =
                item.userId.toString()
            view.findViewById<TextView>(R.id.tStudentName).text = item.name

            lifecycleScope.launch {
                view.findViewById<TextView>(R.id.tStudentGroupTeacher).text =
                    db.groupDao().getGroupFromId(item.groupId).name
            }

            view.findViewById<TextView>(R.id.tPhone).text =
                item.phone

            return view
        }
    }

    private fun showDatePickerDialog(editText: EditText){
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            {
                _, year, month, day ->
                val selectedDate = "$day.${month+1}.$year"
                editText.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}