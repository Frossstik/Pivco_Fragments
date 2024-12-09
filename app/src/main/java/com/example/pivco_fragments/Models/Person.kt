package com.example.pivco_fragments.Models

import java.io.Serializable

data class Person(val mail : String, val password : String) : Serializable {
    companion object {
        var list: ArrayList<Person> = arrayListOf()
    }
}