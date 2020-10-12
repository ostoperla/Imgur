package com.trelp.imgur.domain

enum class Window {
    DAY, WEEK, MONTH, YEAR, ALL;

    override fun toString() = name
}