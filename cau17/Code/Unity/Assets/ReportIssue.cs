using System;
using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;

public class ReportIssue : MonoBehaviour
{
    [SerializeField] private GameObject title;
    [SerializeField] private GameObject description;
    [SerializeField] private GameObject priority;
    [SerializeField] private GameObject ListIssue;
    [SerializeField] private GameObject Panel;
    public void OnButtonPressed()
    {
        CreateIssue dataToSend = new CreateIssue
        {
            title=title.GetComponent<TMP_InputField>().text,
            description=description.GetComponent<TMP_InputField>().text,
            
            project_id= "7",
            priority=priority.GetComponent<TMP_Dropdown>().options[priority.GetComponent<TMP_Dropdown>().value].text,
            reporter= PlayerPrefs.GetString("id")
        };
        NetworkManager.Instance.SendData("issues/create",dataToSend,OnUserCreated);
        Panel.SetActive(false);
    }

    public void OnUserCreated(string result)
    {
        if (result == "true")
        {
            ListIssue.GetComponent<GetIssues>().Init();
        }
        else
        {
            Debug.LogError("Failed to create project");
        }
    }

    [Serializable]
    class CreateIssue
    {
        public string title;
        public string description;
        public string project_id;
        public string priority;
        public string reporter;
    }
}
