package com.example.pivco_fragments

import java.io.Serializable

class Person(_mail : String, _password : String) : Serializable {

    var mail : String = _mail

    var password : String = _password

    companion object {
        var list: ArrayList<Person> = arrayListOf()
    }
}