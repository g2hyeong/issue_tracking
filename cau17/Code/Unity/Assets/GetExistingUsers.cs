using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using TMPro;
using UnityEngine;
using Newtonsoft.Json;

public class GetExistingUsers : MonoBehaviour
{
    public static Dictionary<string, string> Proj= new Dictionary<string, string>();
    public List<User> usersList;

    void Start()
    {
        Init();
    }

    public void Init()
    {
        NetworkManager.Instance.GetData("users",ExistingUsers);
        foreach(Transform child in transform)
        {
            Destroy(child.gameObject);
        }
    }
    public void ExistingUsers(string users)
    {
            List<User> usersList = JsonConvert.DeserializeObject<List<User>>(users);

            foreach (User pj in usersList)
            {
                Proj.Add(pj.userName,pj.project_id);
                GameObject tile = Instantiate(Resources.Load<GameObject>("Prefab/UserList"),
                    transform);
                tile.GetComponent<AdminUsersList>().Init(pj.userName, pj.userPW, pj.userType,pj.project_id);
            }
    }
    
    public class User
    {
        public string id;
        public string userName;
        public string userPW;
        public string userType;
        public string project_id;
    }
}