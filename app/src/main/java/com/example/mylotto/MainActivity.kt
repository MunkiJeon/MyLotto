package com.example.mylotto

import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {
    //화면에서 버튼 & NumberPicker을 찾아 변수화 시킴
    private val clearButton by lazy { findViewById<Button>(R.id.btn_clear) }
    private val addButton by lazy { findViewById<Button>(R.id.btn_add) }
    private val runButton by lazy { findViewById<Button>(R.id.btn_run) }
    private val numPick by lazy { findViewById<NumberPicker>(R.id.np_num) }

    //숨겨둔 번호칸 6개를 List를 만듬
    private val numTextViewList : List<TextView> by lazy {
        listOf<TextView>(
            findViewById(R.id.tv_num1),findViewById(R.id.tv_num2),findViewById(R.id.tv_num3),
            findViewById(R.id.tv_num4),findViewById(R.id.tv_num5),findViewById(R.id.tv_num6),
        )
    }

    //번호 6개 생성 여부
    private var didRun = false
    //NumberPicker로 선택한 수 모음
    private val pickNumberSet = hashSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numPick.minValue = 1
        numPick.maxValue = 45

        initAddButton()
        initRunButton()
        initClearButton()
    }
    private fun initAddButton(){//NumberPicker을 통해 숫자 추가 하는 Function
        addButton.setOnClickListener{
            when {
                //6개가 생성 된후 추가시
                didRun -> showToast("초기화 후에 시도해주세요")
                //5개 이상 선택시
                pickNumberSet.size >= 5 -> showToast("숫자는 최대 5개만 선택 가능합니다.")
                //중복된 숫자 선택시
                pickNumberSet.contains(numPick.value) -> showToast("이미 선택된 숫자입니다.")
                //그외의 경우 번호 추가
                else ->{
                    val textView = numTextViewList[pickNumberSet.size]
                    textView.isVisible = true
                    textView.text = numPick.value.toString()
                    setNumBack(numPick.value, textView)
                    pickNumberSet.add(numPick.value)
                }
            }
        }
    }
    private fun initClearButton(){//NumberPicker와 선택된 숫자 초기화 Function
        clearButton.setOnClickListener{
            pickNumberSet.clear()
            numTextViewList.forEach{ it.isVisible = false }
            didRun = false
            numPick.value = 1
        }
    }

    private fun initRunButton(){//번호 자동 생성 Function
        runButton.setOnClickListener {
            //번호 랜덤 선택 후 list 변수에 담음
            val list = getRandom()
            didRun = true
            list.forEachIndexed{ index, number ->
                //6개의 가려진 숫자공 하나씩 불러옴
                val textView = numTextViewList[index]
                //숫자공에 해당 번호 넣음
                textView.text = number.toString()
                //숫자공 보이게 설정
                textView.isVisible = true
                //숫자공 배경색 입힘
                setNumBack(number, textView)
            }
        }
    }

    private fun getRandom(): List<Int>{//1~45 사이 랜덤한 번호 생성
        //1~45 중 선택되지 않은 숫자 리스트(filter의 리턴 형태 : List<T>)
        val number = (1..45).filter { it !in pickNumberSet }
        //(현재 선택된 숫자 + 1~45 중 선택되지 않은 숫자.섞음.뽑아냄(6-선택된 숫자 갯수)).오름차순 정렬
        // = 현재선택된 숫자 + 랜덤된 선택된 숫자 => 숫자 6개 리턴
        return (pickNumberSet + number.shuffled().take(6-pickNumberSet.size)).sorted()
    }

    private fun setNumBack(number: Int, textView: TextView){ //숫자 공 배경색 지정 function
        //1~10: 노랑, 11~20: 파랑, 21~30: 빨강, 31~40: 회색, 그외: 초록
        val background = when(number){
            in 1..10 -> R.drawable.circle_yellow
            in 11..20 -> R.drawable.circle_bule
            in 21..30 -> R.drawable.circle_red
            in 31..40 -> R.drawable.circle_gray
            else -> R.drawable.circle_green

        }
        //파라메타 값로 들어온 textView의 배경색을 위 background 변수 결과로 정해진 값 넣음
        textView.background = ContextCompat.getDrawable(this, background)
    }

    private fun showToast(message: String){//Toast 문자 출력용 Function
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}

