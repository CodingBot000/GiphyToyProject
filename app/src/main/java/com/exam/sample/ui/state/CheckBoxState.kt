package com.exam.sample.ui.state


/**
 * 체크박스에 대해 초기화 셋팅과 사용자 press에 의한 셋팅을  OnCheckedChangeListener 에서 구분하기 위해서 사용
 */
sealed class CheckBoxState {
    object INIT: CheckBoxState()
    object PRESSED_CHECK: CheckBoxState() // 사용자 터치에 의한  셋팅
    object PRESSED_UNCHECK: CheckBoxState() // 사용자 터치에 의한  셋팅
    object INIT_CHECK: CheckBoxState()   // 초기화 셋팅
    object INIT_UNCHECK: CheckBoxState() // 초기화 셋팅
}
