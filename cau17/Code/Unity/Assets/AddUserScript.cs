using System;
using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;

public class AddUserScript : MonoBehaviour
{
    [SerializeField] private GameObject idObject;
    [SerializeField] private GameObject passwordObject;
    [SerializeField] private GameObject confirmpasswordObject;
    [SerializeField] private GameObject roleObject;
    [SerializeField] private GameObject projectObject;
    
    public void OnButtonPressed()
    {
        Debug.Log(roleObject.GetComponent<TMP_Dropdown>().options[roleObject.GetComponent<TMP_Dropdown>().value].text);
        AddUser dataToSend = new AddUser
        {
            id=idObject.GetComponent<TMP_InputField>().text,
            password=passwordObject.GetComponent<TMP_InputField>().text,
            confirmPassword=confirmpasswordObject.GetComponent<TMP_InputField>().text,
            role=roleObject.GetComponent<TMP_Dropdown>().options[roleObject.GetComponent<TMP_Dropdown>().value].text,
            project= projectObject.GetComponent<TMP_Dropdown>().options[projectObject.GetComponent<TMP_Dropdown>().value].text
        };
        NetworkManager.Instance.SendData("users/create",dataToSend,OnUserCreated);
    }

    public void OnUserCreated(string result)
    {
        if (result == "true")
        {
            idObject.GetComponent<TMP_InputField>().text = String.Empty;
            passwordObject.GetComponent<TMP_InputField>().text = String.Empty;
            confirmpasswordObject.GetComponent<TMP_InputField>().text = String.Empty;
        }
        else
        {
            Debug.LogError("Failed to create project");
        }
    }

    [Serializable]
    class AddUser
    {
        public string id;
        public string password;
        public string confirmPassword;
        public string role;
        public string project;
    }
}