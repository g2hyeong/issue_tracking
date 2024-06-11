using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class LogoutButton : MonoBehaviour
{
    [SerializeField] private GameObject LoginPage;
    [SerializeField] private GameObject AdminPage;
    [SerializeField] private GameObject ProjectsPage;

    public void OnButtonClicked()
    {
        LoginPage.SetActive(true);
        AdminPage.SetActive(false);
        ProjectsPage.SetActive(false);
    }
}
