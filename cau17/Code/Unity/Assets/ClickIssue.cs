using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ClickIssue : MonoBehaviour
{
    [SerializeField] public GameObject IssuePanel;

    public void OnClickIssue()
    {
        IssuePanel.SetActive(true);
        IssuePanel.GetComponent<IssueDetails>().Init();
    }
}
