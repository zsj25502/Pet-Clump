//
//  Profile.swift
//  PetClump
//
//  Created by YSH on 4/22/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import Foundation
import CoreLocation
import Firebase

protocol Profile {
    func generateDictionary() -> [String: Any]
    func upload(vc: QuickAlert, completion: ProfileUploader?)
    func download(uid: String, completion: ProfileDownloader?)
}

protocol ProfileDownloader{
    func didCompleteDownload()
}

protocol ProfileUploader{
    func didCompleteUpload()
}
