package com.example.kothakhali

import com.example.kothakhali.activity.api.ServiceBuilder
import com.example.kothakhali.activity.model.Client
import com.example.kothakhali.activity.model.Comment
import com.example.kothakhali.activity.repository.AdRepository
import com.example.kothakhali.activity.repository.ClientRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class KothaKhaliTest {

    private lateinit var clientRepository: ClientRepository
    private lateinit var adRepository: AdRepository

    @Test
    fun checkLogin() = runBlocking {
        clientRepository = ClientRepository()
        val response = clientRepository.checkclient("saazneu789@gmail.com","1234")
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult,actualResult)
    }

    @Test
    fun checkRegister() = runBlocking {
        val client =Client(name = "Natsu Dragneel",mobile = "9876543210",email = "natsu789@gmail.com",password = "12345")
        clientRepository = ClientRepository()
        val response = clientRepository.clientRegister(client)
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult,actualResult)
    }

    @Test
    fun updateClient() = runBlocking {
        val client =Client(name = "Natsu Zeref")
        clientRepository = ClientRepository()

        ServiceBuilder.token ="Bearer "+ clientRepository.checkclient("natsu789@gmail.com","12345").token
        val response = clientRepository.update(client)
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult,actualResult)
    }

    @Test
    fun deletewishlist() = runBlocking {
        adRepository = AdRepository()
        clientRepository = ClientRepository()

        ServiceBuilder.token ="Bearer "+ clientRepository.checkclient("saazneu789@gmail.com","12345").token
        val response = adRepository.deletewishlist("607b95f0c4514240fc3f51")
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult,actualResult)
    }

    @Test
    fun addcomment() = runBlocking {
        val comment =Comment(adid="60765bc004945d2dd0714ead", comment = "Ramro Cha")
        adRepository = AdRepository()
        clientRepository = ClientRepository()

        ServiceBuilder.token ="Bearer "+ clientRepository.checkclient("natsu789@gmail.com","12345").token
        val response = adRepository.commentadd(comment)
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult,actualResult)
    }
}