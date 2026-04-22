// Automatically generated - do not modify!

package tanstack.history

external interface RouterHistory {
    val location: HistoryLocation
    val length: Int
    val subscribers: js.collections.ReadonlySet<(opts: SubscriberArgs) -> Unit>

    fun subscribe(cb: (opts: SubscriberArgs) -> Unit): () -> Unit

    fun push(
        path: String,
        state: Any? = definedExternally,
        navigateOpts: NavigateOptions = definedExternally,
    ): Unit

    fun replace(
        path: String,
        state: Any? = definedExternally,
        navigateOpts: NavigateOptions = definedExternally,
    ): Unit

    fun go(
        index: Int,
        navigateOpts: NavigateOptions = definedExternally,
    ): Unit

    fun back(navigateOpts: NavigateOptions = definedExternally): Unit

    fun forward(navigateOpts: NavigateOptions = definedExternally): Unit

    fun canGoBack(): Boolean

    fun createHref(href: String): String

    fun block(blocker: NavigationBlocker): () -> Unit

    fun flush(): Unit

    fun destroy(): Unit

    fun notify(action: SubscriberHistoryAction): Unit

    val _ignoreSubscribers: Boolean?
}
