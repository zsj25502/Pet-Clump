//
//  ChatVC.swift
//  AZPeerToPeerConectvity
//
//  Created by Muhammad Afroz on 9/6/17.
//  Copyright © 2017 AfrozZaheer. All rights reserved.
//

import UIKit

class ChatVC: UIViewController {
//
//    @IBOutlet weak var textField: UITextField!
//    @IBOutlet weak var tableView: UITableView!
//    @IBOutlet var toolbar: UIToolbar!
//
//    var messages = [String]()
//
//    var cellIdentifier = "SenderCell"
//    override func viewDidLoad() {
//        super.viewDidLoad()
//
//        tableView.estimatedRowHeight = 70
//        tableView.rowHeight = UITableViewAutomaticDimension
//        textField.delegate = self
//
//    }
//
//    override func didReceiveMemoryWarning() {
//        super.didReceiveMemoryWarning()
//
//    }
//
//    @IBAction func presentMCBrowser(_ sender: Any) {
//
//        //connection.joinSession(vc: self, mcBrowser: nil) // nil == default mcbrowsr
//    }
//    @IBAction func sendText(_ sender: Any) {
//        connection.sendData(data: ["message": textField.text ?? "defaultValue"])
//        messages.append(textField.text!)
//        cellIdentifier = CellIdetifier.sender.rawValue
//        let index = IndexPath(row: messages.count - 1, section: 0)
//        tableView.insertRows(at: [index], with: .automatic)
//        textField.text = "";
//    }
//}
//extension ChatVC: P2PServiceHandlerDelegate {
//    func didRecieve(_ serviceHandler: P2PServiceHandler, data: [String : Any]) {
//
//        DispatchQueue.main.async {
//            if let val = data["message"] {
//                self.cellIdentifier = CellIdetifier.reciever.rawValue
//                self.messages.append(val as! String)
//                let index = IndexPath(row: self.messages.count - 1, section: 0)
//                self.tableView.insertRows(at: [index], with: .automatic)
//
//            }
//        }
//    }
//}
//extension ChatVC: UITextFieldDelegate {
//
//    func textFieldDidBeginEditing(_ textField: UITextField) {
//        if textField != self.textField {
//            textField.inputAccessoryView = toolbar
//        }
//    }
//
//}
//
//
//extension ChatVC: UITableViewDelegate, UITableViewDataSource {
//
//    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
//        let cell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier) as! ChatTableViewCell
//
//        cell.lblText.text = messages[indexPath.row]
//        return cell
//    }
//    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
//        return messages.count
//    }
//}
//
//
//enum CellIdetifier: String {
//    case sender = "SenderCell",
//    reciever = "RecieverCell"
}



