using System;
using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;

public class CreateProjectScript : MonoBehaviour
{
    [SerializeField] private GameObject ProjectName;
    [SerializeField] private GameObject ProjectDescription;
    
    public void OnButtonPressed()
    {
        Project dataToSend = new Project{ title = ProjectName.GetComponent<TMP_InputField>().text,description = ProjectDescription.GetComponent<TMP_InputField>().text};
        NetworkManager.Instance.SendData("projects/create",dataToSend,OnProjectCreated);
    }

    public void OnProjectCreated(string result)
    {
        if (result == "true")
        {
            ProjectName.GetComponent<TMP_InputField>().text = String.Empty;
            ProjectDescription.GetComponent<TMP_InputField>().text = String.Empty;
        }
        else
        {
            Debug.LogError("Failed to create project");
        }
    }

    [Serializable]
    class Project
    {
        public string title;
        public string description;
    }
}
