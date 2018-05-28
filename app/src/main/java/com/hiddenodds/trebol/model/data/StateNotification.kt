package com.hiddenodds.trebol.model.data

enum class StateNotification(val state: Int) {
    STATE_PROCESS(1),
    STATE_FINISH(2),
    STATE_SEND(3);

    companion object{
        fun from(findValue: Int): StateNotification =
                StateNotification.values().first { it.state == findValue }
    }
}
