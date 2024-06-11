using System;
using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;

public class LoginScript : MonoBehaviour
{
    [SerializeField] private GameObject AdminPanel;
    [SerializeField] private GameObject ProjectsPanel;
    [SerializeField] private GameObject LoginFailedString;
    [SerializeField] private GameObject UsernameInput;
    [SerializeField] private GameObject PasswordInput;
    [SerializeField] private GameObject ProjectListView;
    [SerializeField] private GameObject Content;

    void Start()
    {
        AdminPanel.SetActive(false);
        ProjectsPanel.SetActive(false);
    }
    public void OnLoginButtonClicked()
    {
        string username = UsernameInput.GetComponent<TMP_InputField>().text;
        string password = PasswordInput.GetComponent<TMP_InputField>().text;
        LoginInfo dataToSend = new LoginInfo { id = username, password = password };
        NetworkManager.Instance.SendData("users/login",dataToSend,OnLogin);
    }

    void OnLogin(string success)
    {
        if (success == "true")
        {
            PlayerPrefs.SetString("id",UsernameInput.GetComponent<TMP_InputField>().text);
            UsernameInput.GetComponent<TMP_InputField>().text = String.Empty;
            PasswordInput.GetComponent<TMP_InputField>().text = String.Empty;;
            NetworkManager.Instance.SendData("users/isAdmin",new Id{id=PlayerPrefs.GetString("id")},OnAdmin);
        }
        else
        {
            LoginFailedString.SetActive(true);
        }
    }

    void OnAdmin(string isAdmin)
    {
        if (isAdmin == "true")
        {
            ProjectListView.GetComponent<GetExistingProjects>().Init();
            AdminPanel.SetActive(true);
            ProjectsPanel.SetActive(false);
        }
        else
        {
            AdminPanel.SetActive(false);
            ProjectsPanel.SetActive(true);
            Content.GetComponent<GetIssues>().Init();
        }
    }
    
    [Serializable]
    public class LoginInfo
    {
        public string id;
        public string password;
    }

    public class Id
    {
        public string id;
    }
}