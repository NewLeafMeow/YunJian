# 读取文件并提取标签种类
def extract_label_types(file_path):
    labels = set()  # 使用集合自动去重
    
    with open(file_path, 'r', encoding='utf-8') as f:
        for line in f:
            line = line.strip()  # 去除换行符和首尾空格
            if not line:  # 跳过空行
                continue
            # 按第一个逗号分割（避免内容中包含逗号）
            label, _ = line.split(',', 1)  
            labels.add(label)
    
    return list(labels)  # 转换为列表

# 示例用法（需替换实际文件路径）
file_path = '数据1.csv'
label_types = extract_label_types(file_path)
print("提取到的标签种类：", label_types)