package ru.adminmk.myawesomestore.application

import timber.log.Timber

class ReleaseLogger : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
//        if (priority == Log.ERROR || priority == Log.WARN)
//             TODO log(priority, tag, message) to crash library
    }
}
