using System;
using System.Collections;
using System.Collections.Generic;
using Unity.VisualScripting;
using UnityEngine;

public class ReportIssueButton : MonoBehaviour
{
    [SerializeField] private GameObject AddPanel;

    public void OnClickButton()
    {
        AddPanel.SetActive(true);
    }
}
