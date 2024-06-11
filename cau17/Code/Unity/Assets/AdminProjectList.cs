using System;
using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.UI;

public class AdminProjectList : MonoBehaviour
{
    private string project_id;
    public void Init(string id, string title, string description,string currentUserName)
    {
        project_id = id;
        transform.GetChild(0).GetComponent<TMP_Text>().text = title;
        transform.GetChild(1).GetComponent<TMP_Text>().text = description;
    }

    public void OnDeleteProject()
    {
        NetworkManager.Instance.SendData("projects/delete",new Project_id{project_id = project_id},OnSuccessfulDelete);
    }

    public void OnSuccessfulDelete(string result)
    {
        if (result == "true")
        {
            Destroy(gameObject);
        }
    }
[Serializable]
    class Project_id
    {
        public string project_id;
    }
}
