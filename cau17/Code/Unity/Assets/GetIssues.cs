using System;
using System.Collections.Generic;
using System.Linq;
using TMPro;
using UnityEngine;
using Newtonsoft.Json;

public class GetIssues : MonoBehaviour
{
    public List<Issue> issueList;
    
    void Start()
    {
        Init();
    }

    public void Init()
    {
        NetworkManager.Instance.SendData("issues",new ID {id = PlayerPrefs.GetString("id") }, ExistingIssues);
        foreach(Transform child in transform)
        {
            Destroy(child.gameObject);
        }
    }
    public void ExistingIssues(string issues)
    {
        List<Issue> projectList = JsonConvert.DeserializeObject<List<Issue>>(issues);
        foreach (Issue pj in projectList)
        {
            GameObject tile = Instantiate(Resources.Load<GameObject>("Prefab/IssueList"),
                transform);
            tile.transform.GetComponent<ProjIssueList>().Init(pj.id, pj.title,pj.priority, pj.status,pj.reportedDates,pj.reporter,pj.asignee);
        }
    }
    
    public class Issue
    {
        public string id;
        public string title;
        public string description;
        public string priority;
        public string reporter;
        public string asignee;
        public string status;
        public string fixer;
        public string  reportedDates;
    }
    
    [Serializable]
    public class ID
    {
        public string id;
    }
}