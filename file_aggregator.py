# file_aggregator
import csv, os
import shutil
import pandas as pd
import numpy as np
# fileDir = R'C:\Users\akash\Documents\Testing'
# fileOutput = R'C:\Users\akash\Documents\match_scouting_test.csv'
# fileEndDir = R'C:\Users\akash\Documents\endDir'
fileDir = R'C:\Users\Aztechs Robotics\Documents\Match Scouting Data'
fileOutput = R'C:\Users\Aztechs Robotics\Documents\match_scouting_2025.csv'
fileOutput_agg = R'C:\Users\Aztechs Robotics\Documents\match_scouting_2025_agg.csv'
fileOutput_radar = R'C:\Users\Aztechs Robotics\Documents\match_scouting_2025_radar.csv'
fileOutput_comp = R'C:\Users\Aztechs Robotics\Documents\match_scouting_2025_comp.csv'
fileEndDir = R'C:\Users\Aztechs Robotics\Documents\Previously Aggregated Scouting Data'

fileTest = R'C:\Users\Aztechs Robotics\Documents\match_scouting_2025_test.csv'

accepted_comps = {'', 'uvm', 'mayhem', 'battlecry', 'bc'}
uvm = {69, 95, 157, 166, 237, 348, 811, 839, 1058, 1073, 1512, 1729, 2262, 2370, 2423, 2523, 3323, 
       4311, 4572, 4909, 5494, 5687, 6691, 6933, 7913, 8421, 8544, 9096, 9101, 10066, 10108, 10138, 10647}
mayhem = {131, 157, 166, 190, 319, 501, 1073, 1119, 1153, 1721, 1729, 1735, 1768, 1922, 2342, 2648, 
          3623, 3467, 3597, 5813, 5962, 6324, 8046, 9443}
bc = {48, 78, 88, 126, 131, 138, 151, 157, 166, 175, 177, 190, 238, 319, 467, 501, 694, 1058, 1073, 1100, 1155, 1277,
      1474, 1591, 1729, 1735, 1740, 1757, 1768, 1796, 2053, 2079, 2084, 2262, 2265, 2342, 2370, 2423, 2713, 2791, 
      2876, 2877, 3182, 3467, 4041, 4048, 4311, 4575, 4909, 5422, 5687, 6324, 6328, 7153, 7407, 8085, 8544, 8567, 8708,
      8724, 9443, 9642, 10063, 10229}



competition = input("Which competition to select? (Leave blank for all)").lower()




with open(fileOutput, 'a') as output:
    for entry in os.listdir(fileDir):
        filePath = fileDir + '\\'+ entry
        with open(filePath, "r") as file:
            data = ""
            data = file.read()
            output.write(data + "\n")
            file.close()
        shutil.move(filePath, fileEndDir)
    output.close()

data = pd.read_csv(fileOutput)

data['Auto'] = data['Auto #Scored Coral L1']*3 + data['Auto #Scored Coral L2']*4 + data['Auto #Scored Coral L3']*6 + data['Auto #Scored Coral L4']*7 + data['Auto #Scored Barge'] *4 + data['Auto #Scored Processor'] * 6 + data['Left Starting Position'] * 3
data['TeleopCoral'] = data['TeleOp #Scored Coral L1']*2 + data['TeleOp #Scored Coral L2']*3 + data['TeleOp #Scored Coral L3']*4 + data['TeleOp #Scored Coral L4']*5
data['TeleopAlgae'] = data['TeleOp #Scored Barge'] * 4 + data['TeleOp #Scored Processor'] * 2
# renaming data
# E.X. [T#SL1] = TeleOp Number Scored Coral L1
# E.X. [T#SP] = TeleOp Number Scored Barge
if(True):
    data['T#SL1'] = data['TeleOp #Scored Coral L1']
    data['T#SL2'] = data['TeleOp #Scored Coral L2']
    data['T#SL3'] = data['TeleOp #Scored Coral L3']
    data['T#SL4'] = data['TeleOp #Scored Coral L4']
    data['T#SB'] = data['TeleOp #Scored Barge']
    data['T#SP'] = data['TeleOp #Scored Processor']
    data['A#SL1'] = data['Auto #Scored Coral L1']
    data['A#SL2'] = data['Auto #Scored Coral L2']
    data['A#SL3'] = data['Auto #Scored Coral L3']
    data['A#SL4'] = data['Auto #Scored Coral L4']
    data['A#SB'] = data['Auto #Scored Barge']
    data['A#SP'] = data['Auto #Scored Processor']
    

    data['T#AL1'] = data['TeleOp #Attempted Coral L1']
    data['T#AL2'] = data['TeleOp #Attempted Coral L2']
    data['T#AL3'] = data['TeleOp #Attempted Coral L3']
    data['T#AL4'] = data['TeleOp #Attempted Coral L4']
    data['T#AB'] = data['TeleOp #Attempted Barge']
    data['T#AP'] = data['TeleOp #Attempted Processor']
    data['A#AL1'] = data['Auto #Attempted Coral L1']
    data['A#AL2'] = data['Auto #Attempted Coral L2']
    data['A#AL3'] = data['Auto #Attempted Coral L3']
    data['A#AL4'] = data['Auto #Attempted Coral L4']
    data['A#AB'] = data['Auto #Attempted Barge']
    data['A#AP'] = data['Auto #Attempted Processor']
    
mapping = {
    "Park": 2,
    "Hang Deep": 12,
    "Hang Shallow": 6
}
data["Endgame"] = data["Park/Shallow/Deep"].map(mapping).fillna(0).astype(int)

if(competition == 'uvm'):
    df_comp = data[data['Team Number'].isin(uvm)]
    print("Only UVM teams")
elif(competition == 'mayhem'):
    df_comp = data[data['Team Number'].isin(mayhem)]
    print("Only Mayhem teams")
elif(competition == 'battlecry' or competition == 'bc'):
    df_comp = data[data['Team Number'].isin(bc)]
    bc_copy = [x for x in bc if x not in data['Team Number'].tolist()]
    bc_copy.sort()
    print(bc_copy)
    print("Only Battlecry Teams")
else:
    df_comp = data
    print("All teams")



# df_comp.drop(inplace=True, columns=[col for col in df_comp.columns if '#Attempted' in col]+[col for col in df_comp.columns if '#Scored' in col])


col_to_drop = "Competition Location"
value_to_drop = "Test"
data_exclude_test = df_comp[~df_comp[col_to_drop].str.contains(value_to_drop, na=False)]
data_exclude_test.to_csv(fileOutput_comp)

# Match sheet data field
ms_df = data_exclude_test.groupby("Team Number")[["Auto", "TeleopCoral", "TeleopAlgae", "Endgame"] + [col for col in data_exclude_test.columns if 'A#' in col] + [col for col in data_exclude_test.columns if 'T#' in col]].mean(numeric_only=True).reset_index()


ms_df['Auto Score'] = ms_df['Auto']
# ms_df['Auto Acc'] = (ms_df['A#SL1']+ms_df['A#SL2']+ms_df['A#SL3']+ms_df['A#SL4']+ms_df["A#SP"]+ms_df['A#SB'])/(ms_df['A#AL1']+ms_df['A#AL2']+ms_df['A#AL3']+ms_df['A#AL4']+ms_df["A#AP"]+ms_df['A#AB'])
ms_df['Auto Coral'] = ms_df['A#SL1']*3+ms_df['A#SL2']*4+ms_df['A#SL3']*6+ms_df['A#SL4']*7
ms_df['Auto Algae'] = ms_df["A#SP"]*2+ms_df['A#SB']*4

ms_df['Teleop Score'] = ms_df['TeleopCoral'] + ms_df['TeleopAlgae']
ms_df['L1'] = ms_df['T#SL1']*2
ms_df['L2'] = ms_df['T#SL2']*3
ms_df['L3'] = ms_df['T#SL3']*4
ms_df['L4'] = ms_df['T#SL4']*5
ms_df['Processor'] = ms_df['T#SP']*2
ms_df['Barge'] = ms_df['T#SB']*4

ms_df['Endgame Score'] = ms_df['Endgame']
ms_df['Total Score'] = ms_df['Auto']+ms_df['TeleopCoral']+ms_df['TeleopAlgae']+ms_df['Endgame']

ms_df['AutoNorm'] = ms_df['Auto'] / ms_df['Auto'].max()
ms_df['L1Norm'] = ms_df['T#SL1'] / ms_df['T#SL1'].max()
ms_df['L2Norm'] = ms_df['T#SL2'] / ms_df['T#SL2'].max()
ms_df['L3Norm'] = ms_df['T#SL3'] / ms_df['T#SL3'].max()
ms_df['L4Norm'] = ms_df['T#SL4'] / ms_df['T#SL4'].max()
ms_df['TeleopAlgaeNorm'] = ms_df['TeleopAlgae'] / ms_df['TeleopAlgae'].max()
ms_df['EndgameNorm'] = ms_df['Endgame'] / ms_df['Endgame'].max()

ms_df['Auto Coral Acc'] = (ms_df['A#SL1']+ms_df['A#SL2']+ms_df['A#SL3']+ms_df['A#SL4'])/(ms_df['A#AL1']+ms_df['A#AL2']+ms_df['A#AL3']+ms_df['A#AL4'])
ms_df['Auto Algae Acc'] = (ms_df["A#SP"]+ms_df['A#SB'])/(ms_df["A#AP"]+ms_df['A#AB'])
ms_df['L1 Acc']=ms_df['T#SL1']/ms_df['T#AL1']
ms_df['L2 Acc']=ms_df['T#SL2']/ms_df['T#AL2']
ms_df['L3 Acc']=ms_df['T#SL3']/ms_df['T#AL3']
ms_df['L4 Acc']=ms_df['T#SL4']/ms_df['T#AL4']
ms_df['Processor Acc'] = ms_df['T#SP']/ms_df['T#AP']
ms_df['Barge Acc'] = ms_df['T#SB']/ms_df['T#AB']

ms_df.drop(columns=["Auto", "TeleopCoral", "TeleopAlgae", "Endgame"] + [col for col in data_exclude_test.columns if 'A#' in col] + [col for col in data_exclude_test.columns if 'T#' in col], inplace=True)
ms_df.replace(np.inf, 0, inplace=True)
ms_df.to_csv(fileOutput_agg)


# Radar Sheet Data Field
radar_norm = data_exclude_test.groupby("Team Number")[["Auto", "TeleopAlgae", "Endgame", "T#SL1", "T#SL2", "T#SL3", 
                                                       "T#SL4"]].mean().reset_index()
radar_norm['AutoNorm'] = radar_norm['Auto'] / radar_norm['Auto'].max()
radar_norm['L1'] = radar_norm['T#SL1'] / radar_norm['T#SL1'].max()
radar_norm['L2'] = radar_norm['T#SL2'] / radar_norm['T#SL2'].max()
radar_norm['L3'] = radar_norm['T#SL3'] / radar_norm['T#SL3'].max()
radar_norm['L4'] = radar_norm['T#SL4'] / radar_norm['T#SL4'].max()
radar_norm['TeleopAlgaeNorm'] = radar_norm['TeleopAlgae'] / radar_norm['TeleopAlgae'].max()
radar_norm['EndgameNorm'] = radar_norm['Endgame'] / radar_norm['Endgame'].max()

radar_norm.drop(columns=["Auto", "TeleopAlgae", "Endgame", "T#SL1", "T#SL2", "T#SL3", "T#SL4"], inplace=True)

radar_norm = radar_norm[['Team Number', 'TeleopAlgaeNorm','L1','L2','L3','L4','AutoNorm','EndgameNorm']]

radar_norm.to_csv(fileOutput_radar)


print ("Teams: " + str(len(radar_norm)))
print("Process Finished")
