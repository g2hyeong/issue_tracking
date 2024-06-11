using System;
using UnityEngine;
using UnityEngine.Networking;
using System.Collections;
using System.Collections.Generic;

public class NetworkManager : MonoBehaviour
{
    // Singleton instance
    public static NetworkManager Instance { get; private set; }
    private string url = "http://localhost:8080/";

    private void Awake()
    {
        // Ensure only one instance of the NetworkManager exists
        if (Instance == null)
        {
            Instance = this;
            DontDestroyOnLoad(gameObject);
        }
        else
        {
            Destroy(gameObject);
        }
    }
    
    public void SendData(string request, object data,Action<string> callback)
    {
        string jsonData = JsonUtility.ToJson(data);
        StartCoroutine(PostRequest(url+request, jsonData,callback));
    }
    
    public void GetData(string request,Action<string> callback)
    {
        StartCoroutine(GetRequest(url+request,callback));
    }
    
    IEnumerator PostRequest(string uri, string jsonData,Action<string> callback)
    {
        UnityWebRequest webRequest = new UnityWebRequest(uri, "POST");
        byte[] jsonToSend = new System.Text.UTF8Encoding().GetBytes(jsonData);
        webRequest.uploadHandler = new UploadHandlerRaw(jsonToSend);
        webRequest.downloadHandler = new DownloadHandlerBuffer();
        webRequest.SetRequestHeader("Content-Type", "application/json");

        yield return webRequest.SendWebRequest();

        if (webRequest.result == UnityWebRequest.Result.ConnectionError || webRequest.result == UnityWebRequest.Result.ProtocolError)
        {
            Debug.LogError(webRequest.error);
        }
        else
        {
            Debug.Log("Response: " + webRequest.downloadHandler.text);
            callback?.Invoke(webRequest.downloadHandler.text);
        }
    }
    
    IEnumerator GetRequest(string uri,Action<string> callback)
    {
        UnityWebRequest webRequest = UnityWebRequest.Get(uri);

        yield return webRequest.SendWebRequest();

        if (webRequest.result == UnityWebRequest.Result.ConnectionError || webRequest.result == UnityWebRequest.Result.ProtocolError)
        {
            Debug.LogError(webRequest.error);
        }
        else
        {
            Debug.Log("Response: " + webRequest.downloadHandler.text);
            callback?.Invoke(webRequest.downloadHandler.text);
            // Deserialize JSON data if needed
            // YourDataType data = JsonUtility.FromJson<YourDataType>(webRequest.downloadHandler.text);
        }
    }
}
