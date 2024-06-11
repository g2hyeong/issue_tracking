using System;
using System.Collections.Generic;
using System.Linq;
using TMPro;
using UnityEngine;
using Newtonsoft.Json;

public class SearchIssues : MonoBehaviour
{
    public List<Issue> issueList;
    [SerializeField] private GameObject Panel;
    [SerializeField] private GameObject property;
    [SerializeField] private GameObject searchWord;

    public void OnSearch()
    {
        Search dataToSend = new Search
        {
            property=property.GetComponent<TMP_Dropdown>().options[property.GetComponent<TMP_Dropdown>().value].text,
            searchWord=searchWord.GetComponent<TMP_InputField>().text,
        };
        NetworkManager.Instance.SendData("issues/search",dataToSend, ExistingIssues);
        foreach(Transform child in Panel.transform)
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
    public class Search
    {
        public string property;
        public string searchWord;
    }
}