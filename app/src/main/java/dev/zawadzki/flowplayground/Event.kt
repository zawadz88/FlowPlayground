package dev.zawadzki.flowplayground

import androidx.annotation.MainThread

/**
 * Source: https://gist.github.com/JoseAlcerreca/5b661f1800e1e654f07cc54fe87441af?permalink_comment_id=3767036#gistcomment-3767036
 */
class Event<out T>(private val content: T) {

    private val consumedScopes = HashSet<String>()

    fun isConsumed(scope: String = "") = scope in consumedScopes

    @MainThread
    fun consume(scope: String = ""): T? {
        return content.takeIf { !isConsumed(scope) }?.also { consumedScopes.add(scope) }
    }

    fun peek(): T = content
}