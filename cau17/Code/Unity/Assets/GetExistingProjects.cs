using System.Collections.Generic;
using System.Linq;
using TMPro;
using UnityEngine;
using Newtonsoft.Json;

public class GetExistingProjects : MonoBehaviour
{
    public List<Project> projectList;

    [SerializeField] GameObject ProjectDropdown;
    [SerializeField] GameObject ProjectDropdownStat;
    void Start()
    {
        Init();
    }

    public void Init()
    {
        NetworkManager.Instance.GetData("projects",ExistingProjects);
        foreach(Transform child in transform)
        {
            Destroy(child.gameObject);
        }
    }
    public void ExistingProjects(string projects)
    {
        List<Project> projectList = JsonConvert.DeserializeObject<List<Project>>(projects);
            foreach (Project pj in projectList)
            {
                GameObject tile = Instantiate(Resources.Load<GameObject>("Prefab/ProjectList"),
                    transform);
                tile.transform.GetComponent<AdminProjectList>().Init(pj.id, pj.title,pj.description, pj.currentUserName);
            }
            ProjectDropdown.GetComponent<TMP_Dropdown>().AddOptions(projectList.Select(project => project.title).ToList());
            ProjectDropdownStat.GetComponent<TMP_Dropdown>().AddOptions(projectList.Select(project => project.title).ToList());
    }
    
    public class Project
    {
        public string id;
        public string title;
        public string description;
        public string currentUserName;

       // public List<Project> projects;
    }

}
