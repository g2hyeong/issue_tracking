using System;
using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.UI;

public class ProjIssueList : MonoBehaviour
{
    public void Init(string id, string title,string priority, string status,string reportedDates,string reporter,string assignee)
    {
        transform.GetChild(0).GetComponent<TMP_Text>().text = id;
        transform.GetChild(1).GetComponent<TMP_Text>().text = title;
        transform.GetChild(2).GetComponent<TMP_Text>().text = priority;
        transform.GetChild(3).GetComponent<TMP_Text>().text = status;
        transform.GetChild(4).GetComponent<TMP_Text>().text = reportedDates;
        transform.GetChild(5).GetComponent<TMP_Text>().text = reporter;
        transform.GetChild(6).GetComponent<TMP_Text>().text = assignee;
    }

    public void OnIssueClicked()
    {
        
    }

    [Serializable]
    class projID
    {
        public string id;
    }
}