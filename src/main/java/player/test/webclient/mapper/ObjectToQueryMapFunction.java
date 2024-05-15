package player.test.webclient.mapper;

import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ObjectToQueryMapFunction {

    public static MultiValueMap<String, String> apply(Object request) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        change(request, map, "");
        return map;
    }

    @SneakyThrows
    private static void change(Object data, MultiValueMap<String, String> map, String rootName) {
        if (data == null) {
            return;
        }

        if (BeanUtils.isSimpleValueType(data.getClass()) || data instanceof FileSystemResource) {
            String value = data.toString();
            map.add(rootName, value);
        } else {
            if (data instanceof List) {
                List<?> listData = (List<?>) data;
                for (int iter = 0; iter < listData.size(); iter++) {
                    change(listData.get(iter), map, rootName.concat(String.format("[%s]", iter)));
                }
            } else {
                List<Field> fields = Stream.concat(
                        Arrays.stream(data.getClass().getDeclaredFields()),
                        Arrays.stream(data.getClass().getSuperclass().getDeclaredFields())
                ).collect(Collectors.toList());

                for (Field field : fields) {
                    field.setAccessible(true);
                    String fieldName = !rootName.isEmpty()
                            ? rootName.concat(".").concat(field.getName())
                            : field.getName();

                    change(field.get(data), map, fieldName);
                }
            }
        }
    }
}